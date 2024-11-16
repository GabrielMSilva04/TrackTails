import { Outlet } from 'react-router-dom';
import NavBar from './Navbar';


export default function Layout() {
  return (
    <div>
      <div className="fixed w-full">
        <div className="w-32 mx-auto mt-4">
          <h1 className="btn btn-ghost text-xl text-primary mx-auto">trackTails.</h1>
        </div>
      </div>
      <div className="w-full h-svh px-6 pt-16 pb-20">
        <Outlet />
      </div>
      <NavBar />
    </div>
  );
}