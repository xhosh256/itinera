import { Routes, Route, Link } from 'react-router-dom'
import About from "./pages/About.jsx";
import MainPage from "./pages/MainPage.jsx";
import LoginPage from "./pages/auth/LoginPage.jsx";
import RegisterPage from "./pages/auth/RegisterPage.jsx";
import ExplorePage from "./pages/ExplorePage.jsx";
import MyEventsPage from "./pages/MyEventsPage.jsx";
import CreateEventPage from "./pages/CreateEventPage.jsx";
import ManageEventPage from "./pages/ManageEventPage.jsx";
import CreateComponentPage from "./pages/CreateComponentPage.jsx";
import EditEventPage from "./pages/EditEventPage.jsx";
import EditComponentPage from "./pages/EditComponentPage.jsx";
import SearchEventsPage from "./pages/SearchEventsPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
import FriendRequestsPage from "./pages/FriendRequestsPage.jsx";
import InviteFriendsToEvent from "./pages/InviteFriendsToEvents.jsx";
import EventInvitesPage from "./pages/EventInvitesPage.jsx";
import EditProfilePage from "./pages/EditProfilePage.jsx";

function App() {
      return (
          <>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/about" element={<About />} />
                <Route path="/explore" element={<ExplorePage />} />

                <Route path="/profile/:userId" element={<ProfilePage />} />
                <Route path="/profile/edit/:profileId" element={<EditProfilePage />} />

                <Route path="/auth/login" element={<LoginPage />} />
                <Route path="/auth/register" element={<RegisterPage />} />

                <Route path="/events" element={<MyEventsPage />} />
                <Route path="/events/:id" element={<ManageEventPage />} />
                <Route path="/events/create" element={<CreateEventPage />} />
                <Route path="/events/:id/edit" element={<EditEventPage />} />

                <Route path="/events/:id/components/create" element={<CreateComponentPage />} />
                <Route path="/events/:eventId/components/:componentId/edit" element={<EditComponentPage />} />

                <Route path="/events/search" element={<SearchEventsPage/>}/>
                <Route path="/events/invites" element={<EventInvitesPage/>}/>
                <Route path="/events/:eventId/invite" element={<InviteFriendsToEvent/>}/>

                <Route path="/friends/requests" element={<FriendRequestsPage />} />
            </Routes>
          </>

  )
}

export default App