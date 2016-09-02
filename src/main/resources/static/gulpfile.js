var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var argv = require('yargs').argv;
var compatibility = ['last 2 versions', 'ie >= 9'];
var sequence = require('run-sequence');
var gutil = require('gulp-util');

var isGziped = !!(argv.gzip);
var isProduction = !!(argv.production);

var PATHS = {
  dist: './assets',
  bootstrap_fonts: [
    'bower_components/bootstrap-sass/assets/fonts/bootstrap/**/*'
  ]
};

// Compiles the sass files and appends the css files from vendors in one app.css file
gulp.task('stylesheets', function() {

  var minifycss = $.if(isProduction, $.minifyCss());

  var sass =
    gulp
    .src('sass/app.sass')
    .pipe($.sourcemaps.init())
    .pipe($.sass().on('error', $.sass.logError))
    .pipe($.autoprefixer({
      browsers: compatibility
    }))
    .pipe(minifycss)
    .pipe($.if(!isProduction, $.sourcemaps.write()))
    .pipe($.concat('app.css'))
    .pipe(gulp.dest(PATHS.dist));

  return sass;
});

gulp.task('messages', function() {
  if (!isProduction && !isGziped) {
    gutil.log(gutil.colors.yellow("DEVELOPMENT build was finished!"));
  } else if (isProduction && !isGziped) {
    gutil.log(gutil.colors.green("PRODUCTION build without GZIP was finished!"));
  } else if (isProduction && isGziped) {
    gutil.log(gutil.colors.green("PRODUCTION with GZIP build was finished!"));
  }
});

// Build the "dist" folder by running all of the below tasks
gulp.task('default', ['messages'], function() {
  gulp.watch(['./sass/**/*.sass'], ['stylesheets']);
});
