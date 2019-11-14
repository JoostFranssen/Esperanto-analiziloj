class NavigationButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selected: props.selected,
        }
    }

    render() {
        return <button
            className={["navigation-button", this.state.selected ? "selected" : ""].join(" ")}
            onClick={this.props.onClick}
        >
            {this.props.text}
        </button>
    }
}