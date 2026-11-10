import './ErrorStamp.css'

const ErrorStamp = ({children}) => {
    return (
        <p className="error__stamp">{children}</p>
    )
}

export default ErrorStamp;