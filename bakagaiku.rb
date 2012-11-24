#!/usr/bin/ruby1.9.1
# -*- coding: utf-8 -*-

require 'cgi'
require 'erb'

class BakaCgi
  def initialize
    @cgi = CGI.new
  end

  def has_key?(key)
    return @cgi.has_key?(key) && !@cgi[key].empty?
  end

  def [](key)
    return nil unless has_key?(key)
    return @cgi[key]
  end

  def exit_404
    header = @cgi.header("status" => "NOT_FOUND")
    @cgi.print(header)
    templ = File.read("../cgi-data/bakagainai.txt.euc-jp",
                      {:encoding => "euc-jp"})
    erb = ERB.new(templ)
    @cgi.print(erb.result(binding))
    exit
  end

  def out(mtime, str)
    @cgi.out(mtime){str}
  end
end

class ArticlePicker
  def initialize(dir)
    @dir = dir
  end

  def pick(bakaid)
    bakaid = get_latest_bakaid if invalid_bakaid?(bakaid)
    filename = bakaid_to_filename(bakaid)
    begin
      opt = Article::get_encoding_opt(bakaid)
      prehtml = File.read(filename, opt)
      mtime = File.mtime(filename)
      return Article.new(bakaid, prehtml, mtime)
    rescue Errno::ENOENT
      return nil
    end
  end

  def invalid_bakaid?(bakaid)
    return /\A\w+\z/ !~ bakaid
  end

  def get_latest_bakaid
    files = Dir.glob(@dir + '/*.txt')
    files.sort!
    return File.basename(files[-1], '.txt')
  end

  def bakaid_to_filename(bakaid)
    return sprintf('%s/%s.txt', @dir, bakaid)
  end
end

class Article
  def initialize(bakaid, prehtml, mtime)
    @bakaid = bakaid
    @prehtml = prehtml
    @mtime = mtime
  end
  attr_accessor :bakaid, :prehtml, :mtime

  def self.utf8?(bakaid)
    return bakaid[0..7].to_i >= 20121119
  end

  def self.get_encoding(bakaid)
    return utf8?(bakaid) ? "utf-8" : "euc-jp"
  end

  def self.get_encoding_opt(bakaid)
    return {:encoding => get_encoding(bakaid)}
  end

  def utf8?
    return self.class.utf8?(@bakaid)
  end

  def get_encoding
    return self.class.get_encoding(@bakaid)
  end

  def get_encoding_opt
    return self.class.get_encoding_opt(@bakaid)
  end

  def get_template_path
    if utf8?
      return "../cgi-data/bakagaita.erb.utf-8"
    else
      return "../cgi-data/bakagaita.erb.euc-jp"
    end
  end
end

class Bakagaiku
  def initialize(cgi, dir)
    @cgi = cgi
    @picker = ArticlePicker.new(dir)
  end

  def main
    article = @picker.pick(@cgi['bakaid'])
    @cgi.exit_404 unless article
    last_modified = get_last_modified(article.mtime)
    permlink = get_permlink(article.bakaid)
    display(last_modified, permlink, article)
  end

  def get_last_modified(mtime)
    last_modified = CGI::rfc1123_date(mtime)
    return {'Last-Modified' => last_modified}
  end

  def get_permlink(bakaid)
    return sprintf("%s?bakaid=%s", ENV['SCRIPT_NAME'], bakaid)
  end

  def display(last_modified, permlink, article)
    templ = read_template(article)
    erb = ERB.new(templ)
    result = erb.result(binding)
    @cgi.out(last_modified, result)
  end

  def read_template(article)
    path = article.get_template_path
    opt = article.get_encoding_opt
    return File.read(path, opt)
  end
end

if $0 == __FILE__
  Bakagaiku.new(BakaCgi.new, '../../pre.d').main
end
