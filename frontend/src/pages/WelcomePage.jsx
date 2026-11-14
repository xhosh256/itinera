import {Link} from "react-router-dom";
import Header from "../components/Header.jsx";
import './WelcomePage.css'
import ItineraStamp from "../components/ItineraStamp.jsx";
import ImgMainPage from "../assets/images/main/main-page.avif";

const WelcomePage = () => {
    return (
        <div className="welcome-page">
            <Header />

            <div className="welcome__page__layout">
                <div className="welcome__page__header__wrapper">
                    <div style={{display: "flex", flexDirection: "column", gap: "10px"}}>
                        <div className="welcome__page__text">
                            <ItineraStamp>ВАШИ СОБЫТИЯ НАЧИНАЮТСЯ ЗДЕСЬ</ItineraStamp>
                            <p className="welcome__page__header">Меньше планирования, больше впечатлений</p>
                            <p className="welcome-page__header__description">Собирайтесь вместе с друзьями либо с единомышленниками и планируйте события вместе</p>
                        </div>

                        <div className="welcome__page__links">
                            <Link className="welcome__page__explore__link" to="/explore">Найти события</Link>
                            <Link className="welcome__page__create__link" to="/events">Создать событие</Link>
                        </div>
                    </div>

                    <img src={ImgMainPage} alt='main-page'/>
                </div>

                <div className="welcome__page__auth__layout">
                    <div className="welcome__page__auth__layout__text">
                        <p>Присоединяйтесь к нам прямо сейчас</p>
                        <p>Вы получите незабываемые впечатления</p>
                    </div>

                    <Link className="welcome__page__auth__layout__register__link" to="/auth/register">Зарегестрироваться</Link>
                </div>
            </div>
        </div>
    )
}

export default WelcomePage;