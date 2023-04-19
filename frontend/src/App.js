import './App.css';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import Home from "./components/Home"
import Login from "./auth/components/Login"
import Register from "./auth/components/Register"
import Profile from "./components/Profile"

function App() {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Login />} />
        <Route exact path="/home" element={<Home />} />
        <Route exact path="/login" element={<Login />} />
        <Route exact path="/register" element={<Register />} />
        <Route exact path="/profile" element={<Profile />} />
      </Routes>
      </Router>
  );
}

export default App;
