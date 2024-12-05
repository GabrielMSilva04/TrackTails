import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCopy } from "@fortawesome/free-regular-svg-icons";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { baseUrl } from "../consts";

const usersBaseUrl = `${baseUrl}/finders/user`;
const animalsBaseUrl = `${baseUrl}/finders/animal`;

export default function Finders() {
    const { deviceId } = useParams();
    const [animal, setAnimal] = useState(null);
    const [information, setInformation] = useState({
        phone: "",
        name: "",
        description: "",
    });
    const [showTooltip, setShowTooltip] = useState(false);

    useEffect(() => {
        if (animal) {
            const { userId, name, beCarefulWith } = animal;

            const fetchOwnerData = async () => {
                try {
                    const response = await fetch(`${usersBaseUrl}/${userId}`);
                    const ownerData = await response.json();
                    const { phoneNumber, displayName } = ownerData;

                    setInformation({
                        phone: phoneNumber,
                        name: displayName,
                        description: beCarefulWith || "No specific information provided.",
                    });
                } catch (error) {
                    console.error("Error fetching owner data:", error);
                }
            };

            fetchOwnerData();
        }
    }, [animal]);

    useEffect(() => {
        const fetchAnimal = async () => {
            try {
                const response = await fetch(`${animalsBaseUrl}/${deviceId}`);
                const animalData = await response.json();

                setAnimal(animalData);
            } catch (error) {
                console.error("Error fetching animal:", error);
            }
        };

        fetchAnimal();
    }, [deviceId]);

    const copyToClipboard = (text) => {
        navigator.clipboard.writeText(text).then(() => setShowTooltip(true));

        setTimeout(() => {
            setShowTooltip(false);
        }, 2000);
    };

    if (!animal) {
        return <div className="text-center text-primary mt-10">Loading...</div>;
    }

    return (
        <div className="bg-white w-11/12 mx-auto mt-8 p-6 rounded-3xl shadow-lg flex flex-col items-center">
            <div className="w-full max-w-md mb-5 relative">
                <label className="block text-primary font-bold text-sm mb-1">Owner Phone Number</label>
                <div className="relative">
                    {showTooltip && (
                        <div className="absolute -top-8 left-1/2 transform -translate-x-1/2 bg-primary text-white text-xs font-semibold px-3 py-1 rounded-lg shadow-md">
                            Copied to clipboard!
                            <div className="absolute bottom-[-5px] left-1/2 transform -translate-x-1/2 w-2 h-2 bg-primary rotate-45"></div>
                        </div>
                    )}

                    <input
                        type="text"
                        placeholder="Phone Number"
                        value={information.phone || "Information not available"}
                        disabled={!information.phone}
                        className="input input-bordered border-2 input-primary h-8 w-full pr-10"
                        readOnly
                    />
                    <FontAwesomeIcon
                        icon={faCopy}
                        className="absolute right-3 top-1/2 transform -translate-y-1/2 text-primary cursor-pointer hover:text-primary-focus"
                        onClick={() => information.phone ? copyToClipboard(information.phone) : null}
                    />
                </div>
            </div>

            <div className="w-full max-w-md mb-5">
                <label className="block text-primary font-bold mb-1 text-sm">Owner Name</label>
                <input
                    type="text"
                    placeholder="Owner Name"
                    value={information.name || "Loading..."}
                    className="input input-bordered border-2 input-primary h-8 w-full"
                    readOnly
                />
            </div>

            <div className="w-full max-w-md mb-5">
                <label className="block text-primary font-bold mb-1 text-sm">Be Careful With This</label>
                <div className="input input-bordered border-2 input-primary h-20 w-full overflow-y-auto p-2">
                    {information.description || "Loading..."}
                </div>
            </div>

            <p className="text-secondary text-center font-semibold mt-6">
                If you found me call my owner, I’m probably lost.
            </p>
        </div>
    );
}