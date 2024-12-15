import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faLocationDot, faPaw, faUser } from '@fortawesome/free-solid-svg-icons';
import {NavLink} from "react-router-dom";

export default function Navbar() {
    return (
        <div className="btm-nav bg-primary rounded-lg mb-3 w-3/4 mx-auto">
            <button className="w-100% h-100%">
                <NavLink to="/mypets" className="tooltip" data-tip="My Animals">
                    {({isActive}) => (
                        <FontAwesomeIcon icon={faPaw} color={isActive ? '#4d7c0f' : 'white'} size={isActive ? '2xl' : 'lg'}/>
                    )}
                </NavLink>
            </button>
            <button className="w-100% h-100%">
                <NavLink to="/map" className="tooltip" data-tip="Map">
                    {({isActive}) => (
                        <FontAwesomeIcon icon={faLocationDot} color={isActive ? '#4d7c0f' : 'white'} size={isActive ? '2xl' : 'lg'}/>
                    )}
                </NavLink>
            </button>
            <button className="w-100% h-100%">
                <NavLink to="/notifications" className="tooltip" data-tip="Notifications">
                    {({isActive}) => (
                        <FontAwesomeIcon icon={faBell} color={isActive ? '#4d7c0f' : 'white'} size={isActive ? '2xl' : 'lg'}/>
                    )}
                </NavLink>
            </button>
            <button>
                <NavLink to="/profile" className="tooltip" data-tip="Profile">
                    {({ isActive }) => (
                        <FontAwesomeIcon icon={faUser} color={isActive ? '#4d7c0f' : 'white'} size={isActive ? '2xl' : 'lg'}/>
                    )}
                </NavLink>
            </button>
        </div>
    );
}
