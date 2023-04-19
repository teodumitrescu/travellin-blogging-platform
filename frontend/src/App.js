import './App.css';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import Home from "./components/Home"
import Login from "./auth/components/Login"
import Register from "./auth/components/Register"
import Profile from "./components/Profile"
import Posts from "./components/Posts"
import Destinations from "./components/Destinations"
import Post from "./components/Post"

function App() {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Login />} />
        <Route exact path="/home" element={<Home />} />
        <Route exact path="/login" element={<Login />} />
        <Route exact path="/register" element={<Register />} />
        <Route exact path="/profile" element={<Profile />} />
        <Route exact path="/posts" element={<Posts />} />
        <Route exact path="/destinations" element={<Destinations />} />
        <Route path="/blogposts/:id" exact component={<Post />} />

      </Routes>
      </Router>
  );
}

export default App;
