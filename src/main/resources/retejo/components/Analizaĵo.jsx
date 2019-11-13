class Analizaĵo extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let analizaĵeroj = this.props.analizaĵo.vorteroj;
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
                {this.props.elektita ? <VortTrajtoj analizaĵo={this.props.analizaĵo}/> : <span></span> }
            </div>
        );
    }

    disigiVorton() {
        return (this.props.analizaĵo.vorteroj.map(v => v.vortero));
    }
}