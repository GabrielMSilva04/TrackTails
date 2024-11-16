import { Outlet } from 'react-router-dom';
import NavBar from './Navbar';


export default function LayoutAnimal() {
    return (
        <div className="bg-primary h-screen flex flex-col">
            <div className="w-full flex justify-center items-center mt-12">
                <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
            </div>
            <div className="avatar placeholder">
                <div className="bg-neutral border-8 border-white text-neutral-content w-32 rounded-full z-10 mx-auto mt-6">
                    <img src="https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg" alt="adsasd"/>
                </div>
            </div>
            <div className="bg-primary">
                <div className="bg-base-100 w-full h-3/4 rounded-t-3xl p-8 flex flex-col items-center absolute bottom-0">
                    <div className="mt-10 text-primary font-bold">
                        Jack
                    </div>
                    <Outlet/>
                </div>
            </div>
            <NavBar />
        </div>
    );
}