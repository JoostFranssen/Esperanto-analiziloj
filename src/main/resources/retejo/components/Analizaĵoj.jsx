class Analizaĵoj extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            elektitaIndico: null,
            komponantoj: null,
        }
    }

    render() {
        return (
            <div className="vort-analizaĵujo">
                {this.kreiKomponantojn()}
            </div>
        );
    }

    kreiKomponantojn() {
        let komponantListo = [];
        if(this.props.analizaĵoj) {
            for(let i = 0; i < this.props.analizaĵoj.length; i++) {
                komponantListo.push(
                    <Analizaĵo
                        analizaĵo={this.props.analizaĵoj[i]}
                        elektita={this.state.elektitaIndico === i}
                        onClick={() => this.elektiKomponanton(i)}
                    />
                );
            }
        }

        this.state.komponantoj = komponantListo;

        if(komponantListo.length > 0 && !this.state.elektitaIndico) {
            this.elektiKomponanton(0);
        }

        return komponantListo;
    }

    elektiKomponanton(indico) {
        let newState = Object.assign({}, this.state);

        if(indico < 0 || !this.state.komponantoj || indico >= this.state.komponantoj.length) {
            newState.elektitaIndico = null;
        } else {
            newState.elektitaIndico = indico;
        }

        if(this.state.elektitaIndico != indico) {
            this.setState(newState);
        }
    }
}