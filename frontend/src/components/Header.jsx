import {Link} from "react-router-dom";
import './Header.css'
import {useContext} from "react";
import AuthContext from "../context/AuthContext.jsx";
import SearchBar from "./SearchBar.jsx";

const Header = () => {

    const { user } = useContext(AuthContext);

    const isAuthenticated = user !== null;

    return (
        <header>
            <div>
                <Link to="/">Itinera</Link>
            </div>

            <div>
                <SearchBar type="events">
                    Найти Событие
                </SearchBar>
            </div>

            <div>
                <Link to="/events">Мои События</Link>
                <Link to="/about">About</Link>

                {!isAuthenticated ? (
                    <Link to="/auth/login">Войти</Link>
                ) : (
                    <Link to={`/profile/${user.id}`}>Профиль</Link>
                )}
            </div>
        </header>
    )
}

export default Header