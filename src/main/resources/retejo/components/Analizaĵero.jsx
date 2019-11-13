class Analizaĵero extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <span
                className="vort-analizaĵeringo"
                tooltip={snakeCaseToTitleCase(this.props.analizaĵero.vorterSpeco)}
            >
                {this.props.analizaĵero.vortero}
            </span>
        );
    }
}