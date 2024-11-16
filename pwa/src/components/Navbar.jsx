import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faHouse, faLocationDot, faPaw, faUser } from '@fortawesome/free-solid-svg-icons';

export default function Navbar() {
    return (
        <div className="btm-nav bg-primary rounded-lg mb-3 w-3/4 mx-auto">
            <button>
                <a className="tooltip my-auto" data-tip="Home">
                    <FontAwesomeIcon icon={faHouse} color="white" />
                </a>
            </button>
            <button>
                <a className="tooltip" data-tip="Map">
                    <FontAwesomeIcon icon={faLocationDot} color="white" />
                </a>
            </button>
            <button>
                <a className="tooltip" data-tip="My Animals">
                    <FontAwesomeIcon icon={faPaw} color="white" />
                </a>
            </button>
            <button>
                <a className="tooltip" data-tip="Notifications">
                    <FontAwesomeIcon icon={faBell} color="white" />
                </a>
            </button>
            <button>
                <a className="tooltip" data-tip="Profile">
                    <FontAwesomeIcon icon={faUser} color="white" />
                </a>
            </button>
        </div>
    );
}
