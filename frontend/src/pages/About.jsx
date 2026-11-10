import Header from "../components/Header.jsx";
import './About.css'
import {Link} from "react-router-dom";
import { FaArrowRightLong } from "react-icons/fa6";
import ItineraStamp from "../components/ItineraStamp.jsx";
import ImgAbout from "../assets/images/about/about-itinera-pic.avif";
import ImgMission from "../assets/images/about/itinera-mission-pic.avif";
import ImgOpp1 from "../assets/images/about/our-opportunities-1.avif";
import ImgOpp2 from "../assets/images/about/our-opportunities-2.avif";
import ImgOpp3 from "../assets/images/about/our-opportunities-3.avif";
import ImgOpp4 from "../assets/images/about/our-opportunities-4.avif";
import Divider from "../components/Divider.jsx";

const About = () => {

    return (
        <div className="about__page">
            <Header />
            <div className="about__page__layout">
                <div className="about__info">
                    <div className="about__info__text">
                        <ItineraStamp>ПРО ITINERA</ItineraStamp>
                        <p className="about__itinera__header">Любое событие начинается с плана</p>
                        <p className="about__itinera__description">Itinera &#8212; платформа для удобной организации общих событий</p>
                        <Link to="/explore" className="about__itinera__explore__link"><p>Исследовать</p> <FaArrowRightLong size={15}/></Link>
                    </div>
                    <img src={ImgAbout} alt="about-itinera"/>
                </div>

                <div className="our__mission__layout">
                    <img src={ImgMission} alt="out-mission"/>
                    <div className="our__mission__layout__text">
                        <ItineraStamp>НАША МИССИЯ</ItineraStamp>
                        <p className="our__mission__header">Упрощать планирование совместных событий и сблизжать людей</p>
                        <p className="our__mission__description">Itinera была создана для того чтобы упростить организацию, планирование и продумывание ивентов, вместо того, чтобы договариваться о чем то в чатах, просто пригласите друзей в событие и отправляйтесь в путь</p>
                    </div>
                </div>

                <div style={{display: "flex", gap: "10px", flexDirection: "column"}}>
                    <ItineraStamp>ВОЗМОЖНОСТИ ПРИЛОЖЕНИЯ</ItineraStamp>

                    <div className="opportunities__info">
                        <div className="opportunities-block">
                            <img src={ImgOpp1}/>
                            <div>
                                <p>Создавать события</p>
                                <p>Планируйте поездки, встречи и другие активности</p>
                            </div>

                        </div>

                        <div className="opportunities-block">
                            <img src={ImgOpp2}/>
                            <div>
                                <p>Приглашайте друзей</p>
                                <p>Находите дрезей и приглашайте их в свои события</p>
                            </div>

                        </div>

                        <div className="opportunities-block">
                            <img src={ImgOpp3}/>
                            <div>
                                <p>Планируйте вместе</p>
                                <p>Придумывайте идеи и добавляйте их в событий</p>
                            </div>

                        </div>

                        <div className="opportunities-block">
                            <img src={ImgOpp4}/>
                            <div>
                                <p>Познавайте новое</p>
                                <p>Находите события которые вас интересуют и принимайте участие</p>
                            </div>
                        </div>
                    </div>


                </div>

                <div className="get__started__wrapper">
                    <div className="get__started__layout">
                        <p className="get__started__header">Начните свое путешествие вместе с Itinera</p>
                        <p className="get__started__description">Собирайте друзей, планируйте события и воплатите свои идеи в реальность</p>
                        <Link to="/auth/register">Начать путь в Itinera</Link>
                    </div>
                </div>

                <Divider/>

                <p className="copyright">© 2026 Itenera</p>
            </div>
        </div>
    )
}

export default About;