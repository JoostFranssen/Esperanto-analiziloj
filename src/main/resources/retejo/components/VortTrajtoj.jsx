class VortTrajtoj extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let componentList = [];
        if(this.props.analizaĵo) {
            for(const trajto of this.props.analizaĵo.trajtoj) {
                componentList.push(
                    <li className="vorta-trajto">{this.formatTrajto(trajto)}</li>
                );
            }
        }

        if(componentList.length > 0) {
            return (
                <ul className="vortaj-trajtoj">
                    {componentList}
                </ul>
            );
        } else {
            return null;
        }
    }

    formatTrajto(trajto) {
        let trajtPartoj = snakeCaseToTitleCase(trajto).split(" ");
        return trajtPartoj[trajtPartoj.length - 1];
    }
}