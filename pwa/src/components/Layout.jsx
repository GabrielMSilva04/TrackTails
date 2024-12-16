import { Outlet } from 'react-router-dom';
import NavBar from './Navbar';


export default function Layout() {
  return (
    <div className="h-full">
      <div className="fixed w-full bg-base-100 z-10">
        <div className="w-32 mx-auto mt-4">
          <h1 className="btn btn-ghost text-xl text-primary mx-auto">trackTails.</h1>
        </div>
      </div>
      <div className="w-full h-full px-6 pt-16 pb-20">
        <div className="overflow-y-auto">
          <Outlet />
        </div>
      </div>
      <div className="bg-base-100 w-full h-20 fixed bottom-0 z-10">
        <NavBar />
      </div>
    </div>
  );
}

