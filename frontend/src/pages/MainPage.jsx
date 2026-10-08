import Header from "../components/Header.jsx";
import WelcomePage from "./WelcomePage.jsx";
import ExplorePage from "./ExplorePage.jsx";
import {useContext} from "react";
import AuthContext from "../context/AuthContext.jsx";

const MainPage = () => {

    const { user } = useContext(AuthContext);

    const isAuthenticated = user !== null;

    return (
        <div>
            <Header />
            {!isAuthenticated ?
                <WelcomePage /> :
                <ExplorePage />
            }
        </div>
    )
}

export default MainPage