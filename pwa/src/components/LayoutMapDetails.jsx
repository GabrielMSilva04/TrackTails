import 'leaflet/dist/leaflet.css';
import { Link, Outlet } from 'react-router-dom';
import { faBackward, faLightbulb, faRoute, faVolumeHigh, faXmarksLines } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const LayoutMapDetails = () => {
    return (
        <div className="relative min-h-screen bg-white">
            <div className="absolute top-0 h-20 bg-primary w-full flex justify-between items-center px-5">
                <FontAwesomeIcon icon={faBackward} color="white" size="1x"/>
                <img src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.vidaativa.pt%2Fuploads%2F2018%2F03%2Fcollie.jpg&f=1&nofb=1&ipt=36327e66c47693373ff1746b678b9b9c3743017cb61a0fda47bdd5d9738b9ad7&ipo=images" className="absolute left-1/2 transform -translate-x-1/2 bottom-[-20px] w-20 h-20 rounded-full border-4 border-primary"/>  {/* Avatar mais abaixo */}
                <div className="text-white text-xs">
                    Battery 100%
                </div>
            </div>
            <Outlet/>
            <div className="absolute bottom-0 h-24 bg-primary w-full">
                <div className="flex justify-around items-center h-full">
                    <div className="text-center text-white">
                        <FontAwesomeIcon icon={faLightbulb} color="white" size="lg"/>
                        <div className="text-sm">Light</div>
                    </div>
                    <div className="text-center text-white">
                        <FontAwesomeIcon icon={faVolumeHigh} color="white" size="lg"/>
                        <div className="text-sm">Sound</div>
                    </div>
                    <div className="text-center text-white">
                        <FontAwesomeIcon icon={faRoute} color="white" size="lg"/>
                        <div className="text-sm">Route</div>
                    </div>
                    <div className="text-center text-white">
                        <FontAwesomeIcon icon={faXmarksLines} color="white" size="lg"/>
                        <div className="text-sm">Fence</div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LayoutMapDetails;


