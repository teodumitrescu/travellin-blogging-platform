import React, {useState} from "react";
import BannerImage from "../Assets/logo_simplu.svg";
import { FiArrowRight } from "react-icons/fi";
import { Link } from "react-router-dom";
import Navbar from "./Navbar";

function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(true); // Change this to false if you want to test the navbar as if the user is not logged in

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  return (
    <div className="home-container">
      {isLoggedIn && (
        <Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
      )}
      <div className="home-banner-container">
        <div className="home-bannerImage-container">
        </div>
        <div className="home-text-section">
          <h1 className="primary-heading">
            New journeys and adventures for you!           </h1>
          <p className="primary-text">
            Was your trip a blast?<br></br>
            Share it so the others can experience it too!
          </p>
          <Link to="/Posts" style={{textDecoration: 'none' }}>
              <button className="secondary-button">
                Get started <FiArrowRight />{" "}
              </button>
          </Link>
        </div>
        <div className="home-image-section">
          <img src={BannerImage} alt="" />
        </div>
      </div>
    </div>
  );
};

export default Home;