import {Link, NavLink} from "react-router-dom";
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
                <Link className="itinera__link" to="/">Itinera</Link>
            </div>

            <div>
                <SearchBar type="events">
                    Найти событие...
                </SearchBar>
            </div>

            <div className="right__side__links">
                <NavLink className="nav-link" to="/events">Мои События</NavLink>
                <NavLink className="nav-link" to="/about">About</NavLink>

                {!isAuthenticated ? (
                    <NavLink className="nav-link" to="/auth/login">Войти</NavLink>
                ) : (
                    <NavLink className="nav-link" to={`/profile/${user.id}`}>Профиль</NavLink>
                )}
            </div>
        </header>
    )
}

export default Header