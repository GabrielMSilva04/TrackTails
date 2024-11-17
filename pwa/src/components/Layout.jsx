import { Outlet, Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHouse, faLocationDot, faPaw, faBell, faUser } from '@fortawesome/free-solid-svg-icons';

export default function Layout() {
    return (
        <>
            <div className="fixed w-full">
                <div className="w-32 mx-auto mt-4">
                    <h1 className="btn btn-ghost text-xl text-primary mx-auto">trackTails.</h1>
                </div>
            </div>
            <div className="w-full h-svh px-6 pt-16 pb-20">
                <Outlet />
            </div>
            <div className="btm-nav bg-primary rounded-lg mb-3 w-3/4 mx-auto">
                <button>
                    <Link to="/" className="tooltip" data-tip="Home">
                        <FontAwesomeIcon icon={faHouse} color="white" />
                    </Link>
                </button>
                <button>
                    <Link to="/map" className="tooltip" data-tip="Map">
                        <FontAwesomeIcon icon={faLocationDot} color="white" />
                    </Link>
                </button>
                <button>
                    <Link to="/animals" className="tooltip" data-tip="My Animals">
                        <FontAwesomeIcon icon={faPaw} color="white" />
                    </Link>
                </button>
                <button>
                    <Link to="/notifications" className="tooltip" data-tip="Notifications">
                        <FontAwesomeIcon icon={faBell} color="white" />
                    </Link>
                </button>
                <button>
                    <Link to="/profile" className="tooltip" data-tip="Profile">
                        <FontAwesomeIcon icon={faUser} color="white" />
                    </Link>
                </button>
            </div>
        </>
    );
}
