import {Link} from "react-router-dom";
import './EventPreview.css';

const EventPreview = (props) => {
    const {id, name, capacity, joined, visibility} = props;

    const progress = (joined/capacity) * 100;

    return (
        <div className="event__preview">
            <div className="event__preview__header">
                <p className={`event__preview__visibility ${visibility.toLowerCase()}`}>{visibility}</p>
                <p>Организатор: </p>
            </div>

            <div className="event__preview__body">
                <p>{name}</p>
                <p>Описаааааааание</p>
            </div>

            <br/>

            <div className="event__preview__info">
                <p>{joined} / {capacity} участников</p>
                <div className="progress-bar">
                    <div
                    className="progress-fill"
                    style={{width: `${progress}%`}}
                    />
                </div>
                <p>Осталось {capacity-joined} мест(а)</p>
            </div>
            <Link to={`/events/${id}`} className="event__link">Подробнее</Link>
        </div>
    )
}

export default EventPreview;