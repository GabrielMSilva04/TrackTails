import { useParams, Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faBell, faHouse, faLightbulb, faLocationDot, faPaw, faRoute, faUser, faVolumeHigh, faXmarksLines } from "@fortawesome/free-solid-svg-icons";
import { Outlet } from "react-router-dom";
import { PiBoundingBoxFill } from "react-icons/pi";

export default function LayoutMapDetails() {
    const { animalName } = useParams();

    return (
        <>
            {/* Barra de Navegação Superior */}
            <div className="fixed top-0 left-0 right-0 bg-primary p-3 w-full h-24 -z-10 flex items-center">
                <div className="max-w-7xl mx-auto flex justify-between w-full">
                    <button>
                        <Link to="/map" className="tooltip" data-tip="Back Map">
                            <FontAwesomeIcon icon={faArrowLeft} color="white"/>
                        </Link>
                    </button>
                    <div className="flex-1 text-center flex flex-col items-center">
                    </div>
                    <div>
                        <span className="text-white font-semibold text-sm">Battery</span>
                    </div>
                </div>
            </div>

            <div className="w-full flex flex-col items-center mt-4 z-20">
                <span className="text-white font-semibold text-lg">{animalName}</span>
                <img
                    src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.espritdog.com%2Fwp-content%2Fuploads%2F2020%2F09%2Fborder-collie-700810_1920.jpg&f=1&nofb=1&ipt=29bc36e03332cdad92cedd625b2d47519ce9c4bda3a261ca261d17cf3d34b5bd&ipo=images"
                    alt="Dog Avatar"
                    className="h-20 w-20 rounded-full border-4 border-primary mt-2"/>
            </div>

            {/* Conteúdo Principal */}
            <div className="absolute right-0 top-20 left-0 bottom-28 -z-10 overflow-auto">
                <Outlet/>
            </div>

            {/* Barra de Navegação Inferior */}
            <div className="fixed bottom-0 left-0 right-0 bg-primary p-3 w-full h-28 z-10 flex items-center">
                <div className="max-w-7xl mx-4 flex justify-around w-full">
                    <div className="text-center">
                        <Link to="/" className="tooltip" data-tip="Home">
                            <FontAwesomeIcon icon={faLightbulb} color="white" size="lg"/>
                            <div className="text-white text-sm mt-1">Light</div>
                        </Link>
                    </div>
                    <div className="text-center">
                        <Link to="/map" className="tooltip" data-tip="Map">
                            <FontAwesomeIcon icon={faVolumeHigh} color="white" size="lg"/>
                            <div className="text-white text-sm mt-1">Sound</div>
                        </Link>
                    </div>
                    <div className="text-center">
                        <Link to="/animals" className="tooltip" data-tip="My Animals">
                            <FontAwesomeIcon icon={faRoute} color="white" size="lg"/>
                            <div className="text-white text-sm mt-1">Route</div>
                        </Link>
                    </div>
                    <div className="text-center">
                        <Link to="/notifications" className="tooltip" data-tip="Notifications">
                            <PiBoundingBoxFill className="ml-1.5" style={{color: 'white', fontSize: '24px'}}/>
                            <div className="text-white text-sm mr-2 mt-1">Fence</div>
                        </Link>
                    </div>
                </div>
            </div>
        </>
    );
}





