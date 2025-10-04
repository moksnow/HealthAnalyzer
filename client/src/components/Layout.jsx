import { Link, Outlet } from "react-router-dom";
import LogoutButton from "../context/LogoutButton"; 

export default function Layout() {
  return (
    <div className="flex flex-col min-h-screen">
      {/* Header */}
      <header className="bg-blue-600 text-white p-4 shadow">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-lg font-bold">🧠 AI CT Scan Analyzer</h1>
          <nav className="space-x-4">
            <Link to="/" className="hover:underline">Home</Link>
            {/* <Link to="/dashboard" className="hover:underline">Dashboard</Link> */}
            <Link to="/profile" className="hover:underline">Profile</Link>  {/* اضافه شده */}
          </nav>
          <LogoutButton />
        </div>
      </header>

      {/* Main content */}
      <main className="flex-grow container mx-auto p-4">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white text-center py-4">
        &copy; {new Date().getFullYear()} AI Med. All rights reserved.
      </footer>
    </div>
  );
}