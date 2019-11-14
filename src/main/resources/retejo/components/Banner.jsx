class Banner extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <img
                src="android-chrome-192x192.png"
                alt="Esperanto"
                className="bannericon"
                id="icon"
                style={{width: this.props.width}}
            />
        );
    }
}

