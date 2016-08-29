var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var argv = require('yargs').argv;
var browser = require('browser-sync');
var compatibility = ['last 2 versions', 'ie >= 9'];
var mainBowerFiles = require('main-bower-files');
var merge = require('merge-stream');
var panini = require('panini');
var rimraf = require('rimraf');
var sequence = require('run-sequence');
var sherpa = require('style-sherpa');
var gutil = require('gulp-util');

var isGziped = !!(argv.gzip);
var isProduction = !!(argv.production);
var port = 8000;

var PATHS = {
  dist: './assets',
  bootstrap_fonts: [
    'bower_components/bootstrap-sass/assets/fonts/bootstrap/**/*'
  ]
};

// Delete the "dist" folder every time a build starts
// gulp.task('clean', function(done) {
//   rimraf(PATHS.dist, done);
// });

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

// Start a server with LiveReload
// gulp.task('server', ['build'], function() {
//   browser.init({
//     server: PATHS.dist,
//     files: [PATHS.dist + '/assets/css/**/*'],
//     middleware: require("connect-gzip-static")(PATHS.dist),
//     port: port
//   });
// });

// Build the "dist" folder by running all of the below tasks
gulp.task('default', ['messages'], function() {
  gulp.watch(['./sass/**/*.sass'], ['stylesheets']);
});
