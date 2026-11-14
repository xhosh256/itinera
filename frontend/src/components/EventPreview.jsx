import {Link} from "react-router-dom";
import './EventPreview.css';
import Divider from "./Divider.jsx";
import { FaUserGroup } from "react-icons/fa6";
import { FaCalendarDay } from "react-icons/fa6";
import { FaLocationDot } from "react-icons/fa6";

const EventPreview = (props) => {
    const {id, name, capacity, joined, visibility, hostUsername} = props;

    const progress = (joined/capacity) * 100;

    const rightForm = (count) => {
        const lastTwo = count % 100;
        const lastOne = count % 10;

        if(lastTwo >= 11 && lastTwo <= 14) {

            return 'мест';
        }

        if(lastOne === 1) {
            return 'место';
        }

        if(lastOne >= 2 && lastOne <= 4) {
            return 'места';
        }

        return 'мест';
    }

    return (
        <div className="event__preview">
            <div className="event__preview__header">
                <p className={`event__preview__visibility ${visibility.toLowerCase()}`}>{visibility}</p>
                <p>Организатор: @{hostUsername}</p>
            </div>

            <div className="event__preview__body">
                <p>{name}</p>
            </div>

            <Divider/>

            <div className="event__preview__info">
                <p><FaLocationDot size={15}/> локаия типр</p>
                <p><FaCalendarDay size={15}/> дата типо </p>
                <p> <FaUserGroup size={15}/> {joined} / {capacity} участников</p>
                <div className="progress-bar">
                    <div
                    className="progress-fill"
                    style={{width: `${progress}%`}}
                    />
                </div>
                <p>Осталось {capacity-joined} {rightForm(capacity-joined)}</p>
            </div>
            <Link to={`/events/${id}`} className="event__link">Подробнее</Link>
        </div>
    )
}

export default EventPreview;