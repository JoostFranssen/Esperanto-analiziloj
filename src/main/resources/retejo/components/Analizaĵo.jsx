class Analizaĵo extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let vorteroj = this.disigiVorton();
        let komponentListo = [];
        if(vorteroj) {
            for(const vortero of vorteroj) {
                komponentListo.push(<b> | </b>);
                komponentListo.push(
                    <Analizaĵero vortero={vortero} />
                )
            }
        }
        return (
            <div className="vort-analizaĵingo">
                {komponentListo.slice(1)}
            </div>
        );
    }

    disigiVorton() {
        return this.props.vorto.split("|");
    }
}