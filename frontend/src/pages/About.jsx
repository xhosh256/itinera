import Header from "../components/Header.jsx";
import './About.css'
import {Link} from "react-router-dom";
import ItineraStamp from "../components/ItineraStamp.jsx";

const About = () => {

    return (
        <div className="about__page">
            <Header />
            <div className="about__page__layout">
                <div className="about__info">
                    <ItineraStamp>ПРО ITINERA</ItineraStamp>
                    <p>Любое событие начинается с плана</p>
                    <p>Itinera &#8212; платформа для удобной организации общих событий</p>
                    <Link to="/explore">Исследовать</Link>
                </div>

                <div>
                    <ItineraStamp>НАША МИССИЯ</ItineraStamp>
                    <p>Itinera была создана для того чтобы упростить организацию, планирование и продумывание ивентов, вместо того, чтобы договариваться о чем то в чатах, просто пригласите друзей в событие и отправляйтесь в путь</p>
                </div>

                <div>
                    <ItineraStamp>ВОЗМОЖНОСТИ ПРИЛОЖЕНИЯ</ItineraStamp>

                    <div className="opportunities__info">
                        <div>
                            <p>Создавать события</p>
                            <p>Планируйте поездки, встречи и другие активности</p>
                        </div>

                        <div>
                            <p>Приглашайте друзей</p>
                            <p>Находите дрезей и приглашайте их в свои события</p>
                        </div>

                        <div>
                            <p>Планируйте совместно</p>
                            <p>Придумывайте идеи и добавляйте их в событий</p>
                        </div>

                        <div>
                            <p>Познавайте новое</p>
                            <p>Находите события которые вас интересуют и принимайте участие</p>
                        </div>
                    </div>


                </div>

                <div>
                    <ItineraStamp>КАК ЭТО РАБОТАЕТ</ItineraStamp>
                    <div>
                        <p>1</p>
                        <p>Создайте событие</p>
                        <p>Настройте событие как вам требуется</p>
                    </div>

                    <div>
                        <p>2</p>
                        <p>Приглашайте друзей</p>
                        <p>Отправляйте приглашения и собирайте всех вместе</p>
                    </div>

                    <div>
                        <p>3</p>
                        <p>Сделайте это вместе</p>
                        <p>Подготовьтесь к событию и проведите время в хорошей компании</p>
                    </div>

                </div>

                <div>
                    <p>Начните свое путешествие вместе с Itinera</p>
                    <p>Собирайте друзей, планируйте события и воплатите свои идеи в реальность</p>
                    <Link to="/auth/register">Начать путь в Itinera</Link>
                </div>
            </div>
        </div>
    )
}

export default About;