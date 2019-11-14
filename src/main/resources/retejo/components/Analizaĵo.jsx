class Analizaĵo extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let analizaĵeroj = this.props.analizaĵo.vorteroj;
        let componentList = [];
        if(analizaĵeroj) {
            for(const analizaĵero of analizaĵeroj) {
                componentList.push(<b>|</b>);
                componentList.push(
                    <Analizaĵero analizaĵero={analizaĵero} />
                )
            }
        }
        return (
            <div
                className={["vort-analizaĵingo", this.props.selected ? "selected" : ""].join(" ")}
                onClick={this.props.onClick}
            >
                {componentList.slice(1)}
                {this.props.selected ? <VortTrajtoj analizaĵo={this.props.analizaĵo}/> : <span></span> }
            </div>
        );
    }
}