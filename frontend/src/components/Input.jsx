const Input = (props) => {
    const {
        id,
        children,
        type='text',
        onChange,
        value = '',
    } = props

    return (
        <>
            <label htmlFor={id}> {children} </label>
            <input value={value} type={type} id={id} onChange={onChange}/>
        </>
    )
}

export default Input;