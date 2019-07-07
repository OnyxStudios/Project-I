package nerdhub.projecti;

@Mod("projecti")
public class ProjectI {

    private static final Logger LOGGER = LogManager.getLogger("ProjectI");

    public ProjectI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
    }

    private void init(FMLCommonSetupEvent event) {
    }

    private void initClient(FMLClientSetupEvent event) {
    }
}
