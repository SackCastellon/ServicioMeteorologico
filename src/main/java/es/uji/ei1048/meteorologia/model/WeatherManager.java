package es.uji.ei1048.meteorologia.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.github.soc.directories.ProjectDirectories;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WeatherManager {

    private static final @NotNull Logger logger = LogManager.getLogger(WeatherManager.class);

    private static final int MAX_DATA_PER_FILE = 7;

    private static final @NotNull ProjectDirectories dirs = ProjectDirectories.from(null, "UJI", "ServicioMeteorologico"); //NON-NLS
    private static final @NotNull Path ROOT = Paths.get(dirs.dataDir);

    private static final @NotNull WeatherManager INSTANCE = new WeatherManager();
    private static final @NotNull DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); //NON-NLS

    private WeatherManager() {
        logger.debug("App directory: {}", ROOT::toAbsolutePath); //NON-NLS
    }

    public static @NotNull WeatherManager getInstance() {
        return INSTANCE;
    }

    /**
     * @throws MaxFileDataExcededException If the save file has reached the max data count.
     */
    public boolean save(final @NotNull WeatherData data) {
        try {
            final @NotNull String country = data.getCity().getCountry().toLowerCase(Locale.ENGLISH).trim();
            final @NotNull Path folder = ROOT.resolve(country);

            if (Files.notExists(folder)) Files.createDirectories(folder);

            @NonNls final @NotNull String city = data.getCity().getName().toLowerCase(Locale.ENGLISH).trim();
            final @NotNull Path file = folder.resolve(city + ".json"); //NON-NLS

            final @NotNull JsonArray json;
            if (Files.notExists(file)) json = new JsonArray();
            else try (final @NotNull BufferedReader in = Files.newBufferedReader(file)) {
                json = new JsonParser().parse(in).getAsJsonArray();
            }

            final @NotNull Gson gson = new Gson();

            StreamSupport.stream(json.spliterator(), false).filter(it -> gson.fromJson(it, WeatherData.class).getDateTime().isEqual(data.getDateTime())).collect(Collectors.toList()).forEach(json::remove);

            json.add(gson.toJsonTree(data, WeatherData.class));

            if (json.size() >= MAX_DATA_PER_FILE)
                throw new MaxFileDataExcededException();

            try (final @NotNull BufferedWriter out = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                gson.toJson(json, out);
                logger.info(String.format("Saved weather data from %s (%s) at %s -> %s (Count: %d, Limit: %d)", //NON-NLS
                        data.getCity().getName(), data.getCity().getCountry(), DATE_TIME_FORMATTER.format(data.getDateTime()), file, json.size(), MAX_DATA_PER_FILE));
            }
        } catch (final IOException e) {
            logger.error("Failed to save weather data", e); //NON-NLS
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de guardado");
            alert.setHeaderText(null);
            alert.setContentText("No se ha podido guardar los datos.");

            alert.showAndWait();
            return false;
        }
        return true;
    }


    public ArrayList<String> getSavedCities() {
        try {

            final @NotNull Path savesFolder = Paths.get(dirs.dataDir);
            List<Path> countries = Files.walk(savesFolder, 5)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            ArrayList<String> res = new ArrayList<>();
            for (Path country : countries
            ) {
                System.out.println(country.toString());
                System.out.println(File.separator);
                String[] ls = country.toString().split(File.separator);
                res.add(ls[ls.length - 1] + " " + ls[ls.length - 2]);
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public @NotNull SaveFile load(final @NotNull String filename) {
        try {
            final @NotNull List<WeatherData> data;
            try (final BufferedReader in = Files.newBufferedReader(Paths.get(filename))) {
                data = new Gson().fromJson(in, new TypeToken<List<WeatherData>>() {
                }.getType());
            }

            return null; // TODO
        } catch (final IOException e) {
            logger.error("Failed to load weather data", e); //NON-NLS
        }
        return null; // TODO
    }
}
