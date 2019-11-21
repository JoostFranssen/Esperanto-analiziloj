class Frazero extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let vortoj = this.props.frazero.vortoj;
        let componentList = [];
        for(const vorto of vortoj) {
            componentList.push(<span>&nbsp;</span>);
            componentList.push(
                <FrazaVorto vorto={vorto}/>
            )
        }
        return (
            <span
                className={["frazero", this.props.selected ? "selected" : "", this.props.related ? "related" : ""].join(" ")}
                onClick={() => this.props.onClick(this.props.frazero)}
                bracket={(this.props.lower ? "\n" : "") + snakeCaseToTitleCase(this.props.funkcio)}
            >
                {componentList.slice(1)}
            </span>
        )
    }
}