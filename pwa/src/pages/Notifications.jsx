import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-regular-svg-icons";
import { useEffect, useState } from "react";
import NotificationComponent from "../components/NotificationComponent";
import { useAnimalContext } from "../contexts/AnimalContext";
import axios from "axios";
import { baseUrl } from "../consts";

const notificationsBaseUrl = `${baseUrl}/notifications`;

export default function Notifications() {
    const [notifications, setNotifications] = useState([]);
    const { animals } = useAnimalContext(); // Access animal context

    const authHeaders = {
        Authorization: `Bearer ${localStorage.getItem("authToken")}`,
    };

    useEffect(() => {
        let timeoutId;
        let isMounted = true; // Track if the component is mounted

        const fetchNotifications = async () => {
            try {
                // Fetch notifications
                const token = localStorage.getItem("authToken");
                const response = await axios.get(`${notificationsBaseUrl}/me`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                const notificationData = response.data;

                // Enrich notifications with animal data from context
                const enrichedNotifications = notificationData.map((notification) => {
                    const animal = animals.find((a) => a.id === notification.animalId) || {};
                    return {
                        ...notification,
                        name: animal.name || "Unknown",
                        notification: notification.content.replace("{name}", animal.name || "Unknown"),
                        imageUrl: animal.imageUrl || "https://placehold.co/150",
                        highlight:
                            !notification.read && notification.title.startsWith("WARN"), // Highlight logic
                    };
                });

                enrichedNotifications.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

                if (isMounted) {
                    setNotifications(enrichedNotifications);

                    // Schedule marking notifications as read
                    timeoutId = setTimeout(() => {
                        if (isMounted) markNotificationsAsRead(enrichedNotifications);
                    }, 10000);
                }
            } catch (error) {
                console.error("Error fetching notifications:", error);
            }
        };

        fetchNotifications();

        // Cleanup function to cancel the timeout and set isMounted to false
        return () => {
            isMounted = false;
            if (timeoutId) {
                clearTimeout(timeoutId);
                console.log("Timeout cleared");
            }
        };
    }, [animals]); // Add animals to dependencies

    const markNotificationsAsRead = async (notificationList) => {
        try {
            await Promise.all(
                notificationList
                    .filter((notification) => !notification.read) // Filter unread notifications
                    .map((notification) =>
                        axios.put(
                            `${notificationsBaseUrl}/${notification.id}`,
                            {
                                ...notification,
                                read: true,
                            },
                            { headers: authHeaders }
                        )
                    )
            );

            // Update state to mark notifications as read
            setNotifications((prev) =>
                prev.map((notification) => ({ ...notification, read: true }))
            );
            console.log("Marked notifications as read");
        } catch (error) {
            console.error("Error marking notifications as read:", error);
        }
    };

    const handleClearAll = async () => {
        try {
            await Promise.all(
                notifications.map((notification) =>
                    axios.delete(`${notificationsBaseUrl}/${notification.id}`, {
                        headers: authHeaders,
                    })
                )
            );

            // Clear notifications from state
            setNotifications([]);
        } catch (error) {
            console.error("Error clearing notifications:", error);
        }
    };

    return (
        <div className="h-full">
            <div className="mt-10 flex justify-between">
                <h1 className="text-primary text-2xl font-extrabold">
                    {notifications.length > 0 ? "Notifications" : "No notifications to show"}
                </h1>
                {notifications.length > 0 && (
                    <button className="text-primary text-2xl" onClick={handleClearAll}>
                        <FontAwesomeIcon icon={faCircleCheck} />
                    </button>
                )}
            </div>

            {/* Scrollable Notifications Container */}
            <div
                className="mt-5 overflow-y-auto"
            >
                {notifications.map((notification) => (
                    <div key={notification.id}>
                        <NotificationComponent
                            id={notification.id}
                            name={notification.name}
                            notification={notification.notification}
                            image={notification.imageUrl}
                            highlight={notification.highlight}
                            onDelete={() =>
                                setNotifications((prev) => prev.filter((n) => n.id !== notification.id))
                            }
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}