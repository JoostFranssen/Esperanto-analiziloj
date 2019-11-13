class VortTrajtoj extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let listoDeKomponantoj = [];
        if(this.props.analizaĵo) {
            for(const trajto of this.props.analizaĵo.trajtoj) {
                listoDeKomponantoj.push(
                    <li className="vorta-trajto">{snakeCaseToTitleCase(trajto)}</li>
                );
            }
        }

        if(listoDeKomponantoj.length > 0) {
            return (
                <ul className="vortaj-trajtoj">
                    {listoDeKomponantoj}
                </ul>
            );
        } else {
            return null;
        }
    }
}