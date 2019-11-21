class Frazo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentFrazero: null,
            relatedFrazeroj: [],
        }
    }

    render() {
        if(this.props.frazo) {
            let frazeroj = this.props.frazo.frazeroj;
            let componentList = [];
            let shouldLower = false;
            let previousLabel = false;
            if(frazeroj) {
                for(const frazero of frazeroj) {
                    let shouldSelect = frazero === this.state.currentFrazero;
                    let shouldRelate = (this.state.relatedFrazeroj.some(f => f.index === frazero.index));

                    let hasSuccessiveLabels = previousLabel && (shouldSelect || shouldRelate);
                    if(hasSuccessiveLabels) {
                        shouldLower = !shouldLower;
                    } else {
                        shouldLower = false;
                    }

                    previousLabel = shouldSelect || shouldRelate;

                    componentList.push(<span>&nbsp;</span>);
                    componentList.push(
                        <Frazero
                            frazero={frazero}
                            onClick={this.handleFrazeroClick.bind(this)}
                            selected={shouldSelect}
                            related={shouldRelate && frazeroj.includes(this.state.currentFrazero)}
                            funkcio={shouldRelate ? this.state.relatedFrazeroj.find(f => f.index === frazero.index).funkcio : frazero.funkcio}
                            lower={shouldLower}
                        />
                    );
                }
            }

            return (
                <div
                    className="frazo"
                >
                    {componentList.slice(1)}
                </div>
            );
        } else {
            return null;
        }
    }

    handleFrazeroClick(frazero) {
        this.setState({
            currentFrazero: frazero,
            relatedFrazeroj: frazero.relatedFrazeroj,
        });
    }
}