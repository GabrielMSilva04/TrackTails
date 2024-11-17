import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCopy } from "@fortawesome/free-regular-svg-icons";
import { useState } from "react";

export default function Finders() {
    const information = {
        phone: 987654322,
        name: "John Doe",
        description: "The dog is usually scared when with people he doesn't know",
    };

    const [showTooltip, setShowTooltip] = useState(false);

    const copyToClipboard = (text) => {
        navigator.clipboard.writeText(text).then(() => setShowTooltip(true));

        setTimeout(() => {
            setShowTooltip(false);
        }, 2000);
    };

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
                        value={information.phone}
                        className="input input-bordered border-2 input-primary h-8 w-full pr-10"
                        readOnly
                    />
                    <FontAwesomeIcon
                        icon={faCopy}
                        className="absolute right-3 top-1/2 transform -translate-y-1/2 text-primary cursor-pointer hover:text-primary-focus"
                        onClick={() => copyToClipboard(information.phone)}
                    />
                </div>
            </div>

            <div className="w-full max-w-md mb-5">
                <label className="block text-primary font-bold mb-1 text-sm">Owner Name</label>
                <input
                    type="text"
                    placeholder="Owner Name"
                    value={information.name}
                    className="input input-bordered border-2 input-primary h-8 w-full"
                    readOnly
                />
            </div>

            <div className="w-full max-w-md mb-5">
                <label className="block text-primary font-bold mb-1 text-sm">Be Careful With This</label>
                <div className="input input-bordered border-2 input-primary h-20 w-full overflow-y-auto p-2">
                    {information.description}
                </div>
            </div>

            <p className="text-secondary text-center font-semibold mt-6">
                If you found me call my owner, I’m probably lost.
            </p>
        </div>
    );
}