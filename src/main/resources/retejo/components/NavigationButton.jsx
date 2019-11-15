class NavigationButton extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <button
            className={["navigation-button", this.props.selected ? "selected" : ""].join(" ")}
            onClick={this.props.onClick}
        >
            {this.props.text}
        </button>
    }
}