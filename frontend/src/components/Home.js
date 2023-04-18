import React from "react";
import BannerBackground from "../Assets/mesh3.png";
import BannerImage from "../Assets/logo_simplu.svg";
import { FiArrowRight } from "react-icons/fi";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div className="home-container">
      <div className="home-banner-container">
        <div className="home-bannerImage-container">
          <img src={BannerBackground} alt="" />
        </div>
        <div className="home-text-section">
          <h1 className="primary-heading">
            New journeys and adventures for you!           </h1>
          <p className="primary-text">
            Was your trip a blast?<br></br>
            Share it so the others can experience it too!
          </p>
          <Link to="/Login" style={{textDecoration: 'none' }}>
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