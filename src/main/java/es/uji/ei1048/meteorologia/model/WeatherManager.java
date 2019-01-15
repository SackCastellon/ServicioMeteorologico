package es.uji.ei1048.meteorologia.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.github.soc.directories.ProjectDirectories;
import javafx.scene.control.Alert;
import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WeatherManager {

    private static final @NotNull Logger logger = LogManager.getLogger(WeatherManager.class);

    public static final int MAX_DATA_PER_FILE = 7;

    private static final @NotNull ProjectDirectories dirs = ProjectDirectories.from(null, "UJI", "ServicioMeteorologico"); //NON-NLS

    public static Path getDataDir() {
        return DATA_DIR;
    }

    private static final @NotNull Path DATA_DIR = Paths.get(dirs.dataDir);

    private static final @NotNull WeatherManager INSTANCE = new WeatherManager();
    private static final @NotNull DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); //NON-NLS

    private WeatherManager() {
        logger.debug("AppData directory: {}", DATA_DIR::toAbsolutePath); //NON-NLS
    }

    public static @NotNull WeatherManager getInstance() {
        return INSTANCE;
    }

    /**
     * @throws MaxFileDataExceededException If the save file has reached the max data count.
     */
    public boolean save(final @NotNull WeatherData data) {
        try {
            final @NotNull String country = data.getCity().getCountry().toLowerCase(Locale.ENGLISH).trim();
            final @NotNull Path folder = DATA_DIR.resolve(country);

            if (Files.notExists(folder)) Files.createDirectories(folder);

            @NonNls final @NotNull String city = data.getCity().getName().toLowerCase(Locale.ENGLISH).trim();
            final @NotNull Path file = folder.resolve(city + ".json"); //NON-NLS

            final @NotNull JsonArray json;
            if (Files.notExists(file)) {
                Files.createFile(file);
                json = new JsonArray();
            } else try (final @NotNull BufferedReader in = Files.newBufferedReader(file)) {
                json = new JsonParser().parse(in).getAsJsonArray();
            }

            final @NotNull Gson gson = new Gson();

            StreamSupport.stream(json.spliterator(), false).filter(it -> gson.fromJson(it, WeatherData.class).getDateTime().isEqual(data.getDateTime())).collect(Collectors.toList()).forEach(json::remove);

            json.add(gson.toJsonTree(data, WeatherData.class));

            if (json.size() > MAX_DATA_PER_FILE)
                throw new MaxFileDataExceededException();

            try (final @NotNull BufferedWriter out = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                gson.toJson(json, out);
                logger.info(String.format("Saved weather data from: %s (%s) - %s -> %s (Count: %d, Limit: %d)", //NON-NLS
                        data.getCity().getName(), data.getCity().getCountry(), DATE_TIME_FORMATTER.format(data.getDateTime()), file, json.size(), MAX_DATA_PER_FILE));
            }
        } catch (final IOException e) {
            logger.error("Failed to save weather data", e); //NON-NLS

            final @NotNull ResourceBundle resources = ResourceBundle.getBundle("bundles/LoadWeather"); //NON-NLS

            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("error.save.title"));
            alert.setHeaderText(null);
            alert.setContentText(resources.getString("error.save.content.failed"));

            alert.showAndWait();
            return false;
        }
        return true;
    }

    public @NotNull List<@NotNull String> getSavedCities() {
        try (final @NotNull Stream<Path> walk = Files.walk(DATA_DIR)) {
            return walk
                    .filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .map(path -> {
                        final @NotNull String country = path.getParent().getFileName().toString();
                        final @NotNull String city = path.getFileName().toString().split("\\.")[0];
                        return String.format("%s (%s)", WordUtils.capitalize(city), country.toUpperCase(Locale.ENGLISH)); //NON-NLS
                    })
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            logger.error("Failed to get saved cities list.", e); //NON-NLS
            return Collections.emptyList();
        }
    }


    public @NotNull List<WeatherData> load(final @NotNull String query) {
        try {
            final String[] sep = query.split(" ");
            final String city = sep[0].toLowerCase(Locale.ENGLISH);
            final String country = sep[1].substring(1, 3).toLowerCase(Locale.ENGLISH);
            final Path filepath = DATA_DIR.resolve(country).resolve(city + ".json"); //NON-NLS
            try (final BufferedReader in = Files.newBufferedReader(filepath)) {
                return new Gson().fromJson(in, new ListTypeToken().getType());
            }
        } catch (final IOException e) {
            logger.error("Failed to load weather data", e); //NON-NLS
            return Collections.emptyList();
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to process query", e);
        }
    }

    private static class ListTypeToken extends TypeToken<List<WeatherData>> {
    }
}
