import {Link} from "react-router-dom";

const EventPreview = (props) => {
    const {id, name, capacity, participantIds } = props;

    return (
        <div>
            <p>{name}: {participantIds.length}/{capacity}</p>
            <Link to={`/events/${id}`}>Подробнее</Link>
        </div>
    )
}

export default EventPreview;