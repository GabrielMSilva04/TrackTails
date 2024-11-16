import { Outlet } from 'react-router-dom';
import NavBar from './Navbar';


export default function LayoutAnimal() {
  return (
    <div className="bg-primary h-screen flex flex-col overflow-hidden">
      <div className="w-full flex justify-center items-center mt-12">
        <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
      </div>
      <div className="avatar placeholder justify-center">
        <div className="bg-neutral border-8 border-base-100 text-neutral-content w-32 rounded-full z-10 mx-auto mt-2 absolute">
          <img src="https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg" alt="adsasd" />
        </div>
      </div>
      <div className="h-full pt-24">
        <div className="bg-base-100 w-full h-full rounded-t-3xl flex flex-col items-center pb-44">
          <div className="mt-8 mb-2 text-primary font-bold">
            Jack
          </div>
          <div className="py-1 h-full overflow-y-auto">
            <Outlet />
          </div>
        </div>
      </div>
      <NavBar />
    </div>
  );
}