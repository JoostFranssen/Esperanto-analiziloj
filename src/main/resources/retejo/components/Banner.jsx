class Banner extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="banner-container">
                <img
                    src="android-chrome-192x192.png"
                    alt="Esperanto"
                    className="bannericon"
                    id="icon"
                    style={{width: this.props.width}}
                />
                <div className="banner-text-container">
                    <div className="banner-text">Esperanto-Analiziloj</div>
                </div>
            </div>
        );
    }
}
