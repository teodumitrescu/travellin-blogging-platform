import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../Navbar.css';

function Navbar({ isLoggedIn, handleLogout }) {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem('token');
    handleLogout();
    navigate('/login');
  };

  return (
    <nav className='navbar'>
      <div className='navbar__brand'>
        <Link to='/'>BLOG AROUND THE GLOBE</Link>
      </div>
      {isLoggedIn && (
        <ul className='navbar__list'>
          <li>
            <Link to='/profile' className='navbar__link'>Profile</Link>
          </li>
          <li>
            <Link to='/posts' className='navbar__link'>Posts</Link>
          </li>
          <li>
            <Link to='/destinations' className='navbar__link'>Destinations</Link>
          </li>
          <li>
            <Link to='/login' onClick={logout} className='navbar__link navbar__link--logout'>
              Logout
            </Link>
          </li>
        </ul>
      )}
    </nav>
  );
}

export default Navbar;
