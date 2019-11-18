class Frazero extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let vortoj = this.props.frazero.vortoj;
        let componentList = [];
        for(const vorto of vortoj) {
            componentList.push(
                <FrazaVorto vorto={vorto}/>
            )
        }
        return (
            <span
                className={["frazero", this.props.selected ? "selected" : ""].join(" ")}
                onClick={() => this.props.onClick(this.props.frazero)}
            >
                {componentList}
            </span>
        )
    }
}