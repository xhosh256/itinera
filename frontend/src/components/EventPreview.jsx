import {Link} from "react-router-dom";
import './EventPreview.css';

const EventPreview = (props) => {
    const {id, name, capacity, joined, visibility} = props;

    return (
        <div className="event__preview">
            <p>{visibility}</p>
            <p>{name}: {joined}/{capacity} participants</p>
            <Link to={`/events/${id}`}>Подробнее</Link>
        </div>
    )
}

export default EventPreview;