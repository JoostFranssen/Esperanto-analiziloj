class FrazaVorto extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <span
                className="fraz-vorto"
            >
                {this.props.vorto.vorto}
            </span>
        );
    }
}