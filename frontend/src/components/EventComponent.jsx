import './EventComponent.css'

const EventComponent = (props) => {
    const {
        startTime,
        endTime,
        description,
        address
    } = props;

    return (
        <div className="event__component">
            <p>{startTime} - {endTime}</p>
            <p>{description}</p>
            <p>{address}</p>
        </div>
    )
}

export default EventComponent;