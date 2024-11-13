import { Outlet, Link } from 'react-router-dom';

export default function Layout() {
  return (
    <div>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/about">About</Link>
          </li>
        </ul>
      </nav>
      <div className="container">
        <main>
          <Outlet />
        </main>
      </div>
    </div>
  );
}