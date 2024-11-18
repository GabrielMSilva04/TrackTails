import { Outlet, Link } from 'react-router-dom';
import NavBar from './Navbar';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";


export default function LayoutAnimal({ showButtons = "all" }) {
  const animal = {
    name: "Jack",
    img: "https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg"
  }
  /* Added show button prop to make it possible to use the layout in different pages:
    - showButtons={"all"}: Shows the edit and delete buttons for pet monitoring page
    - showButtons={"back-only"}: Shows only the back button for the historical data page
    - showButtons={"none"}: Doesn't show any button for finders page
 */

  LayoutAnimal.propTypes = {
    showButtons: PropTypes.oneOf(["all", "back-only", "none"])
  }

  return (
    <div className="bg-primary h-screen flex flex-col overflow-hidden">
      {showButtons !== "none" && (
        <Link to="/" className="text-white text-2xl font-bold absolute left-4 mt-5 z-10 fixed">
          <FontAwesomeIcon icon={faArrowLeft} />
        </Link>
      )}
      <div className="w-full flex justify-center items-center mt-4">
        <h1 className="text-3xl text-white font-bold tracking-wide">trackTails.</h1>
      </div>
      <div className="avatar placeholder justify-center">
        <div className="bg-neutral border-8 border-base-100 text-neutral-content w-32 rounded-full z-10 mx-auto mt-4 absolute">
          <img src="https://ilovemydogsomuch.com/wp-content/uploads/2023/02/323839-1600x1066-border-collie-breed-1400x933.jpg" alt="adsasd" />
        </div>
      </div>
      <div className="h-full pt-20">
        <div className="bg-base-100 w-full h-full rounded-t-3xl flex flex-col pb-36 items-center">
          {showButtons === "all" && (
            <button className="text-lg text-red-700 border rounded-full border-red-700 w-7 h-7 m-2 ml-auto z-20">
              <FontAwesomeIcon icon={faTrash} />
            </button>
          )}
          {showButtons === "all" ? (
            <div className="mt-4 text-primary font-bold text-2xl items-center justify-center">
              {animal.name}
              <button className="ml-1.5 text-lg text-neutral border rounded-full border-neutral w-7 h-7 items-center justify-center">
                <FontAwesomeIcon icon={faEdit} />
              </button>
            </div>
          ) : (
            <div className="mt-14 text-primary font-bold text-2xl items-center justify-center">
              {animal.name}
            </div>
          )}

          <div className="overflow-y-auto">
            <Outlet />
          </div>
        </div>
      </div>
        {showButtons !== "none" && <NavBar />}
    </div>
  );
}
