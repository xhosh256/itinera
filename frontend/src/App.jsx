import { Routes, Route, Link } from 'react-router-dom'
import About from "./pages/About.jsx";
import MainPage from "./pages/MainPage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import ExplorePage from "./pages/ExplorePage.jsx";
import MyEventsPage from "./pages/MyEventsPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";

function App() {
      return (
          <>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/about" element={<About />} />
                <Route path="/explore" element={<ExplorePage />} />

                <Route path="/profile" element={<ProfilePage />} />

                <Route path="/auth/login" element={<LoginPage />} />
                <Route path="/auth/register" element={<RegisterPage />} />

                <Route path="/events" element={<MyEventsPage />} />
            </Routes>
          </>

  )
}

export default App