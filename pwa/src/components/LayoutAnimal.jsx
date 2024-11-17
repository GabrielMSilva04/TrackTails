import { Link, Outlet } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";

export default function LayoutAnimal({ showButtons = "all" }) {
    const animal = {
        name: "Jack",
        img: "https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg"
    };

    /* Added show button prop to make it possible to use the layout in different pages:
        - showButtons={"all"}: Shows the edit and delete buttons for pet monitoring page
        - showButtons={"back-only"}: Shows only the back button for the data page
        - showButtons={"none"}: Doesn't show any button for finders page
     */

    LayoutAnimal.propTypes = {
        showButtons: PropTypes.oneOf(["all", "back-only", "none"])
    }

    return (
        <div className="bg-primary h-screen flex flex-col">
            {showButtons !== "none" && (
                <Link to="/" className="text-white text-2xl font-bold absolute left-4 mt-5">
                    <FontAwesomeIcon icon={faArrowLeft} />
                </Link>
            )}
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>
            <div className="avatar placeholder">
                <div className="bg-neutral border-8 border-white text-neutral-content w-32 rounded-full z-10 mx-auto mt-8">
                    <img src={animal.img} alt={animal.name} />
                </div>
            </div>
            <div className="bg-primary">
                <div className="bg-white w-full h-3/4 rounded-t-3xl p-1 flex flex-col items-center absolute bottom-0">
                    <div className="mt-7 text-primary font-bold text-2xl items-center justify-center">
                        <span>{animal.name}</span>
                        {showButtons === "all" && (
                            <>
                                <button className="absolute top-3 right-3 text-lg text-red-700 border rounded-full border-red-700 w-7 h-7 items-center justify-center">
                                    <FontAwesomeIcon icon={faTrash} />
                                </button>
                                <button className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center">
                                    <FontAwesomeIcon icon={faEdit} />
                                </button>
                            </>
                        )}
                    </div>
                    <div className="overflow-y-auto">
                        <Outlet />
                    </div>
                </div>
            </div>
        </div>
    );
}