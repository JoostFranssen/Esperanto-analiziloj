class Analizaĵo extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let analizaĵeroj = this.disigiVorton();
        let komponentListo = [];
        if(analizaĵeroj) {
            for(const analizaĵero of analizaĵeroj) {
                komponentListo.push(<b>|</b>);
                komponentListo.push(
                    <Analizaĵero analizaĵero={analizaĵero} />
                )
            }
        }
        return (
            <div
                className={["vort-analizaĵingo", this.props.elektita ? "elektita" : ""].join(" ")}
                onClick={this.props.onClick}
            >
                {komponentListo.slice(1)}
            </div>
        );
    }

    disigiVorton() {
        return (this.props.analizaĵo.vorteroj.map(v => v.vortero));
    }
}